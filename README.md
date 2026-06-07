# PROJ26E5 - Projeto POO + SI

## Gestão da Cantina

### Funcionalidades Disponíveis

- Criar utilizador como Cliente ou Funcionário
- Fazer Login
- Abrir Monitor de Pedidos

#### Como Funcionário

- Entregar pedidos pendentes, alterando o estado do pedido
- Criar, visualizar e eliminar itens
- Criar ementas
- Adicionar itens com stock à ementa
- Visualizar ementas
- Ver lista de clientes
- Ver lista de funcionários
- Criar Relatório das Vendas/Pedidos

#### Como Cliente

- Visualizar Ementa do Dia
- Criar pedido diário com os itens da ementa do dia
- Visualizar estado do pedido
- Ver histórico dos pedidos

## Como Correr o Projeto

Antes de abrir o Cliente ou o Monitor de Pedidos, o Servidor deve estar a correr e aberto.

### Criar os ficheiros JAR

Na pasta do projeto, correr:

```bash
mvn clean package
```
Depois de criar os ficheiros JAR, estes ficam disponíveis na pasta target (as vezes é necessário alterar o nome dos .jar)
Se quiser, mude o nome dos ficheiros jar
- Proj26E5-1.0-SNAPSHOT-main -> Cliente.jar
- Proj26E5-1.0-SNAPSHOT-monitor-cliente -> Monitor.jar
- Proj26E5-1.0-SNAPSHOT.servidor -> Servidor.jar

### macOS / Linux
Abrir a pasta do projeto no Terminal. 

#### Servidor
```bash
java -jar target/Servidor.jar
```
#### Cliente
```bash
java -jar target/Cliente.jar
```
#### Monitor de Pedidos
```bash
java -jar target/Monitor.jar
```

### Windows
Abrir a pasta do projeto no CMD ou PowerShell, ou duplo clique no ficheiro que deseja abrir!

#### Servidor
```bash
java -jar target/Servidor.jar
```

#### Cliente
```bash
java -jar target/Cliente.jar
```

#### Monitor de Pedidos
```bash
java -jar target/Monitor.jar
```

### Membros do Grupo
- Guilherme Graça - 53861
- Diana - 53267
- Simão Gonçalves - 53570
- Arthur - 53987
- Rafael Calheiros - 53828
