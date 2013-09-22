<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>Listagem de Contas</title>
</head>
<body>
	<div class="container container-page">
		<div id="erro" class="alert alert-error" style="display:none;">
			<button type="button" class="close">&times;</button>
			<span id="mensagemErro"></span>
		</div>
		
		<div id="sucesso" class="alert alert-success" style="display:none;">
			<button type="button" class="close">&times;</button>
			<span id="mensagemSucesso"></span>
		</div>
		
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Descri��o</th>
					<th>Tipo de Conta</th>
					<th>Data</th>
					<th>Valor</th>
					<th>A��es</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${contas}" var="conta">
					<c:choose>
						<c:when test="${conta.tipoConta == 'CREDITO'}">
							<tr style="color: blue;">
						</c:when>
						<c:otherwise>
							<tr style="color: red;">
						</c:otherwise>
					</c:choose>
						<td>${conta.descricao}</td>
						<td>${conta.tipoConta.descricao}</td>
						<td>${conta.data}</td>
						<td>R$ ${conta.valor}</td>
						<td class="acoes">
							<a href="<c:url value='/conta/atualiza/${conta.id}' />"><img title="Editar" src=<c:url value='/images/editar.png' /> /></a>
							<input type="hidden" id='urlDelete${index.index}' value="<c:url value='/conta/deleta/${conta.id}' />" />
							<a href="#" id="delete${index.index}"><img title="Deletar" src="<c:url value='/images/delete.png' />" /></a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<script src="<c:url value="/js/jquery-1.8.1.min.js"/>"></script>
	<script src="<c:url value="/js/conta/conta.js"/>"></script>
</body>
</html>