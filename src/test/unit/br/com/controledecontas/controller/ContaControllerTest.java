package br.com.controledecontas.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.util.test.MockLocalization;
import br.com.caelum.vraptor.util.test.MockSerializationResult;
import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.ValidationException;
import br.com.controledecontas.model.Conta;
import br.com.controledecontas.model.TipoConta;
import br.com.controledecontas.model.Usuario;
import br.com.controledecontas.model.UsuarioSession;
import br.com.controledecontas.service.ContaService;
import br.com.controledecontas.wrapper.ContaWrapper;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;


public class ContaControllerTest {

	private MockSerializationResult result;
	private ContaController controller;
	private Validator validator;
	private Localization localization;

	@Mock
	private ContaService service;
	@Mock
	private UsuarioSession usuarioSession;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		result = new MockSerializationResult();
		validator = new MockValidator();
		localization = new MockLocalization();
		controller = new ContaController(result, service, usuarioSession, validator, localization);
	}
	
	@Test
	public void deveriaAbrirTelaInicialComContasDoUltimoMes() {
		List<Conta> listaDeContas = new ArrayList<Conta>();
		
		when(service.pesquisaPorMesEAno(any(Usuario.class), anyInt(), anyInt())).thenReturn(listaDeContas);
		
		controller.index();
		
		assertTrue("Deve haver uma lista de contas.", result.included().containsKey("contas"));
	}
	
	@Test
	public void deveriaAbrirPaginaDeAdicionarComListaDeTipoDeConta() {
		controller.novo();
		
		assertTrue("Deve haver uma lista de tipos de Conta.", result.included().containsKey("tiposConta"));
	}
	
	@Test
	public void deveriaAbrirPaginaDeAtualizacao() {
		Conta conta = conta();
		
		when(service.pesquisaPorId(conta.getId())).thenReturn(conta);
		
		controller.edita(conta.getId());
		
		assertTrue("Deve haver uma conta", result.included().containsKey("conta"));
		assertTrue("Deve conter uma lista de tipo de conta", result.included().containsKey("tiposConta"));
	}
	
	@Test
	public void deveriaSalvarConta() {
		Conta conta = conta();
		
		when(usuarioSession.getUsuario()).thenReturn(criaUsuario());
		
		controller.salvar(conta);
		
		verify(service).salva(conta);
		verify(usuarioSession).getUsuario();

		assertNotNull("Usuário não deve ser nulo", conta.getUsuario());
		assertEquals(criaUsuario().getId(), conta.getUsuario().getId());
		assertEquals(localization.getMessage("conta.salva.sucesso"), result.included().get("notice"));
		assertFalse("Não deveria conter mensagem de erro.", result.included().containsKey("erros"));
	}
	
	@Test(expected = ValidationException.class)
	public void naoDeveriaSalvarContaVazia() {
		Conta conta = contaVazia();
		
		when(usuarioSession.getUsuario()).thenReturn(criaUsuario());
		
		controller.salvar(conta);
	}
	
	@Test
	public void naoDeveriaSalvarContaException() {
		Conta conta = conta();
		
		when(usuarioSession.getUsuario()).thenReturn(criaUsuario());
		doThrow(new RuntimeException()).when(service).salva(conta);
		
		controller.salvar(conta);
		
		assertTrue("Deveria conter mensagem de erro.", result.included().containsKey("erro"));
		assertFalse("Não deveria conter mensagem de successo.", result.included().containsKey("notice"));
	}

	@Test
	public void deveriaAtualizarUmaConta() {
		Conta conta = conta();

		controller.atualizar(conta);
		
		verify(service).atualiza(any(Conta.class), any(Conta.class));
		verify(usuarioSession).getUsuario();
		
		assertTrue("Deveria conter uma mensagem de sucesso", result.included().containsKey("notice"));
		assertEquals(localization.getMessage("conta.atualizada.sucesso"), result.included().get("notice"));
		assertFalse("Não deveria conter uma mensagem de erro", result.included().containsKey("erro"));
	}
	
	@Test(expected = ValidationException.class)
	public void naoDeveriaAtualizarUmaContaVazia() {
		Conta conta = contaVazia();
		
		controller.atualizar(conta);
	}
	
	@Test
	public void naoDeveriaAtualizarUmaContaException() {
		Conta conta = conta();
		
		doThrow(new RuntimeException()).when(service).atualiza(any(Conta.class), any(Conta.class));
		
		controller.atualizar(conta);
		
		assertTrue("Deveria conter mensagem de erro.", result.included().containsKey("erro"));
		assertFalse("Não deveria conter mensagem de successo.", result.included().containsKey("notice"));
	}
	
	@Test
	public void deveriaDeletarUmaConta() throws Exception {
		Conta conta = conta();
		
		when(usuarioSession.getUsuario()).thenReturn(criaUsuario());
		when(service.pesquisaPorId(conta.getId())).thenReturn(conta);
		
		controller.deletar(conta.getId());

		ContaWrapper wrapper = new ContaWrapper(conta, localization.getMessage("conta.removida.sucesso"));
		
		verify(service).deleta(conta);
		
		XStream xstream = getXStream(false, true);
		
		assertEquals(xstream.toXML(wrapper), result.serializedResult());
		assertFalse("Não deveria conter uma mensagem de erro", result.included().containsKey("erro"));
	}
	
	@Test
	public void naoDeveriaDeletarUmaContaException() throws Exception {
		Conta conta = conta();
		
		when(service.pesquisaPorId(conta.getId())).thenReturn(conta);
		doThrow(new RuntimeException("ERRO")).when(service).deleta(conta);
		
		controller.deletar(conta.getId());
		
		verify(service).deleta(conta);
		
		XStream xstream = getXStream(false, true);
		
		assertEquals(xstream.toXML("ERRO"), result.serializedResult());
	}
	
	@Test
	public void deveriaPesquisarContaPorPeriodo() {
		List<Conta> contas = Lists.newArrayList(conta());
		
		when(service.pesquisarPorPeriodo(any(Usuario.class), any(Date.class), any(Date.class))).thenReturn(contas);
		
		controller.pesquisaPorPeriodo(new Date(), new Date());
		
		verify(usuarioSession).getUsuario();
		
		assertTrue("Deve conter uma lista de contas.", result.included().containsKey("contas"));
	}

	@Test
	public void deveriaPesquisarContaPorDescricao() {
		List<Conta> contas = Lists.newArrayList(conta());
		
		when(service.pesquisaPorDescricao(any(Usuario.class), any(String.class))).thenReturn(contas);
		
		controller.pesquisaPorDescricao("Crédito");
		
		verify(usuarioSession).getUsuario();
		
		assertTrue("Deve conter uma lista de contas.", result.included().containsKey("contas"));
	}

	private Conta conta() {
		Conta conta = new Conta();
		
		conta.setId(-1);
		conta.setData(Calendar.getInstance().getTime());
		conta.setDescricao("Cartão de Crédito");
		conta.setTipoConta(TipoConta.DEBITO);
		conta.setValor(new BigDecimal("102.00"));
		
		return conta;
	}

	private Conta contaVazia() {
		Conta conta = new Conta();
		
		conta.setDescricao("");
		conta.setValor(new BigDecimal("0.00"));
		conta.setData(null);
		
		return conta;
	}
	
	private Usuario criaUsuario() {
		Usuario usuario = new Usuario();
		
		usuario.setId(1);
		usuario.setNome("Renan");
		usuario.setUsername("renanigt");
		usuario.setPassword("teste");
		
		return usuario;
	}

	private XStream getXStream(Boolean indented, final Boolean withoutRoot) {
		String DEFAULT_NEW_LINE = "";
	    char[] DEFAULT_LINE_INDENTER = {};
	    
	    String INDENTED_NEW_LINE = "\n";
	    char[] INDENTED_LINE_INDENTER = {' ', ' '};
	    
        final String newLine = (indented ? INDENTED_NEW_LINE : DEFAULT_NEW_LINE);
        final char[] lineIndenter = (indented ? INDENTED_LINE_INDENTER : DEFAULT_LINE_INDENTER);

        XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
            public HierarchicalStreamWriter createWriter(Writer writer) {
                if (withoutRoot) {
                    return new JsonWriter(writer, lineIndenter, newLine, JsonWriter.DROP_ROOT_MODE);
                }

                return new JsonWriter(writer, lineIndenter, newLine);
            }
        });
        
        xstream.setMode(XStream.NO_REFERENCES);
        
        return xstream;
    }
	
}
