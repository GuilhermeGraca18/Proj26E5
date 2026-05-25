# PROJ26E5 - Projeto POO + SI

## Gestão da Cantina

### Funcionalidades Disponíveis

- Criar utilizador como Cliente ou Funcionário
- Fazer Login

#### Como Funcionário

- Entregar pedidos pendentes, alterando o estado do pedido
- Criar, visualizar e eliminar itens
- Criar ementas
- Adicionar itens com stock à ementa
- Visualizar ementas
- Ver lista de clientes
- Ver lista de funcionários

#### Como Cliente

- Criar pedido de teste para visualizar no Monitor de Pedidos

## Como Correr o Projeto

Antes de abrir o Cliente ou o Monitor de Pedidos, o Servidor deve estar a correr e aberto.

### Criar os ficheiros JAR

Na pasta do projeto, correr:

```bash
mvn clean package
```
Depois de criar os ficheiros JAR, estes ficam disponíveis na pasta target

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
Abrir a pasta do projeto no CMD ou PowerShell.

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
- Guilherme Gomes Graça - 53861
- Diana - 53267
- Simão - 53570
- Arthur - 53987
- Rafael Calheiros - 53828
