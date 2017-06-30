# treeProject

Processo para execução

OBS: precisei no meu servidor mysql executar "SET GLOBAL time_zone = '+3:00';", pois o driver jdbc mysql acusava um erro sem isso. 

-clonar projeto

-configurar usuario root e senha root no mysql server e criar banco com nome tree

-executar mvn clean install (maven)

-fazer deploy do war gerado na pasta target

-acessar url /tree

:D
