<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<style type="text/css">
		.container-login {
			width: 400px;
			margin-left: auto;
			margin-right: auto;
		}
	</style>
</head>
<body>
	<div class="container-page container-login">
		<c:if test="${notice != null}">
			<div id="sucesso" class="alert alert-success">
				<button type="button" class="close">&times;</button>
				<span id="mensagemErro">${notice}</span>
			</div>
		</c:if>
	
		<fieldset>
			<legend>Login</legend><br />
			<form class="form-horizontal" action="<c:url value="/login/logar" />" name="form_usuario" method="post">
				<div class="control-group">
					<label class="control-label">Login:</label>
					<div class="controls">
						<input type="text" name="login" id="login" size="15" maxlength="15" placeholder="Login" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Senha:</label>
					<div class="controls">
						<input type="password" name="senha" id="senha" size="10" maxlength="10" placeholder="Senha" />
					</div>
				</div>
				<div class="controls">
					<input class="btn btn-primary" type="submit" value="Logar" />
					<a class="btn btn-primary" href="<c:url value="/usuario/novo"/>">Cadastro</a>
				</div>
			</form>
		</fieldset>
	</div>
	
	<script src="<c:url value="/js/login/login.js"/>"></script>
</body>
</html>