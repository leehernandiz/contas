<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>Adicionar usu�rio !</title>
</head>
<body>

	<span>
		<c:forEach var="error" items="${errors}"><li>${error.category} - ${error.message}</li></c:forEach>
	</span>

	<fieldset>
		<legend>Adicionar Usu�rio</legend>
		<form class="form-horizontal" action="<c:url value="/usuario/novo/salvar" />" name="form_usuario" method="post">
			<div class="control-group">
				<label class="control-label">Nome:</label>
				<div class="controls">
					<input type="text" name="usuario.nome" id="nome" size="60" maxlength="60"
						value="${usuario.nome}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Username:</label>
				<div class="controls">
					<input type="text" name="usuario.username" id="username" size="15" maxlength="15"
						value="${usuario.username}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Password:</label>
				<div class="controls">
					<input type="password" name="usuario.password" id="password" size="10" maxlength="10" />
				</div>
			</div>
			<div class="controls">
				<input class="btn btn-primary" type="submit" value="Salvar" />
			</div>
		</form>
	</fieldset>

</body>
</html>