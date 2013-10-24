<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<html>
<head>
	<title>Listagem de Contas</title>
</head>
<body>
	<div class="container container-page">
		<c:choose>
			<c:when test="${erro != null}">
				<div id="erro" class="alert alert-error">
			</c:when>
			<c:otherwise>
				<div id="erro" class="alert alert-error" style="display:none;">
			</c:otherwise>
		</c:choose>
			<button type="button" class="close">&times;</button>
			<span id="mensagemErro">${erro}</span>
		</div>
		
		<c:choose>
			<c:when test="${notice != null}">
				<div id="sucesso" class="alert alert-success">
			</c:when>
			<c:otherwise>
				<div id="sucesso" class="alert alert-success" style="display:none;">
			</c:otherwise>
		</c:choose>
			<button type="button" class="close">&times;</button>
			<span id="mensagemSucesso">${notice}</span>
		</div>
		
		<div style="text-align: center;">
			<h3>Saldo:
				<c:choose>
					<c:when test="${usuarioSession.usuario.saldo < 0}">
						<span style="color: red;" id="saldo">
					</c:when>
					<c:when test="${usuarioSession.usuario.saldo > 0}">
						<span style="color: blue;" id="saldo">
					</c:when>
					<c:otherwise>
						<span id="saldo">
					</c:otherwise>
				</c:choose>
					<fmt:formatNumber value="${usuarioSession.usuario.saldo}" minFractionDigits="2" type="currency"/> 
				</span>
			</h3>
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
				<c:forEach items="${contas}" var="conta" varStatus="index">
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
						<td><fmt:formatNumber value="${conta.valor}" minFractionDigits="2" type="currency"/></td>
						<td class="acoes">
							<a href="<c:url value='/conta/atualiza/${conta.id}' />"><img title="Editar" src=<c:url value='/images/editar.png' /> /></a>
							<a class="delete" href="<c:url value='/conta/deleta/${conta.id}' />"><img title="Deletar" src="<c:url value='/images/delete.png' />" /></a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<script src="<c:url value="/js/conta/conta.js"/>"></script>
</body>
</html>