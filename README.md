# Instruções de funcionamento:

Antes de correr este programa é necessário realizar alguns passos, nomeadamente a preparação da base de dados e a configuração dos ficheiros jar e alguns pré-requisitos:
Pré-requisitos:
 - Java jdk 19 (windows 11), jdk 17(macOs) (versões com que testamos) com Language level 11
 - PostgreSQL
 - IntelliJ

Quanto à base de dados é necessário criar a bd, para tal comece por abrir a linha de comandos na pasta do ficheiro e corra o seguinte comando:
```sh
psql -h locahost -p 5432 -U postgres
```

Ao correr o comando faça login com pass default que definiu na instalação do postgres.

De seguida crie uma nova base de dados com o nome sddb (o nome tem de ser exatamente este) conforme a imagem abaixo.

```sql
create database sddb;
```

De seguida crie um utilizador de nome adminsd e password admin (novamente, os nomes têm de ser exatamente estes):

```sql
create user adminsd password 'admin';
```

Depois feche a linha de comandos e abra outra vez na pasta do ficheiro e entre com o seguinte comando, digitando de seguida a password do utilizador que criou:

Figura 5 - comando para aceder à nova base de dados
```sh
psql -h localhost -p 5432 -U adminsd -d sddb;
```
Depois para preencher a base de dados corra o ficheiro Comandos.sql conforme a imagem seguinte:

```sql
\i Comandos.sql
```
De seguida temos de adicionar os ficheiros jar necessários para a execução do programa, para tal abra o programa no intelij, vá a file e de seguida abra o project structure. Aí vá à aba Libraries e carregue no simbolo ‘+’ e selecione java:

De seguida  tem de ir à pasta do projeto e escolher o ficheiro jsoup-1.15.4.jar e carregar em ok. Faça o mesmo procedimento para o jar postgresql-42.5.4. Depois é so carregar em aplicar e já tem tudo configurado. Para correr o programa dê build e corra o programa pela seguinte ordem -> QueueUrls -> SearcheModule e a partir daí é indiferente a ordem que escolher, no entanto tem que ter atenção aos barrels e aos downloaders que precisam de um argumento iniciarem (id de cada barrel/downloader). Por isso recomendamos que corra os ficheiros pela linha de comandos. Para obter o comando para correr cada ficheiro inicie a QueueUrls pelo intelij e copie o caminho que lhe vai aparecer que será do género:
```cmd
C:\Users\conta\.jdks\openjdk-19.0.2\bin\java.exe "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.2.2\lib\idea_rt.jar=35600:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.2.2\bin" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\conta\OneDrive\Ambiente de Trabalho\SistemasDistribuidos\out\production\SistemasDistribuidos;C:\Users\conta\OneDrive\Ambiente de Trabalho\SistemasDistribuidos\jsoup-1.15.4.jar;C:\Users\conta\OneDrive\Ambiente de Trabalho\SistemasDistribuidos\postgresql-42.5.4.jar" Message.QueueUrls
```

Este é o comando que tem de correr na linha de comandos para abrir o ficheiro QueueUrls, mas para abrir os outros ficheiros basta trocar no final deste comando a parte que diz QueueUrls pelo nome do ficheiro que quer abrir, e se for um barrel ou um downloader depois do nome acrescentar um espaço e depois o id. Segue um exemplo do que seria o comando para o downloader:

```cmd
C:\Users\conta\.jdks\openjdk-19.0.2\bin\java.exe "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.2.2\lib\idea_rt.jar=35600:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.2.2\bin" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\conta\OneDrive\Ambiente de Trabalho\SistemasDistribuidos\out\production\SistemasDistribuidos;C:\Users\conta\OneDrive\Ambiente de Trabalho\SistemasDistribuidos\jsoup-1.15.4.jar;C:\Users\conta\OneDrive\Ambiente de Trabalho\SistemasDistribuidos\postgresql-42.5.4.jar" Message.Downloaders 5555
```

Assim, tem tudo o que precisa para correr a aplicação.

