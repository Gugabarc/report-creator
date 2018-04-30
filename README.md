## Problema

Você deve criar um sistema de análise de dados onde o sistema deve poder importar lotes de arquivos, ler e analisar os dados e produzir um relatório. Existem 3 tipos de dados dentro desses arquivos. Para cada tipo de dados há um layout diferente.

### Dados do vendedor

Os dados do vendedor têm o formato id 001​ e a linha terá o seguinte formato.

001çCPFçNameçSalary

### Dados do cliente

Os dados do cliente têm o formato id 002​ e a linha terá o seguinte formato.

002çCNPJçNameçBusiness Area

### Dados de vendas

Os dados de vendas têm o formato id 003​. Dentro da linha de vendas, existe a lista
de itens, que é envolto por colchetes []. A linha terá o seguinte formato.

003çSale IDç[Item ID-Item Quantity-Item Price]çSalesman name

### Dados de Exemplo

O seguinte é um exemplo dos dados que o sistema deve ser capaz de ler.

001ç1234567891234çDiegoç50000

001ç3245678865434çRenatoç40000.99

002ç2345675434544345çJose da SilvaçRural

002ç2345675433444345çEduardo PereiraçRural

003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego

003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato

### Análise de dados

Seu sistema deve ler dados do diretório padrão, localizado em% HOMEPATH% /
data / in. O sistema só deve ler arquivos .dat.

Depois de processar todos os arquivos dentro do diretório padrão de entrada, o sistema deve criar um arquivo dentro do diretório de saída padrão, localizado em% HOMEPATH%/data/out. O nome do arquivo deve siga este padrão, {flat_file_name}.done.dat.

O conteúdo do arquivo de saída deve resumir os seguintes dados:

- Quantidade de clientes no arquivo de entrada
- Quantidade de vendedor no arquivo de entrada
- ID da venda mais cara
- O pior vendedor

Este sistema deve ser trabalhar o tempo todo. Todos os arquivos novos estar disponível, tudo deve ser executado O seu código estiver escrito em Java. Você tem total liberdade para utilizar google com o que você precisa. Sinta-se à vontade para escolher qualquer biblioteca externa se for necessário.

Critérios de Avaliação
- Clean Code
- Simplicity
- Logic
- SOC (Separation of Concerns)
- Flexibility/Extensibility
- Scalability/Performance

## Solução

Para resolver o problema proposto utilizei o framework Spring Batch, que aliado ao Spring Boot, oferece uma série de recursos para tornar a aplicação mais robusta, permitindo que o desenvolvedor tenha maior foco na implementação das regras de negócio. 

Para executar a aplicação basta rodar o método main da classe ReportCreatorApplication.

No arquivo properties é possível definir os diretórios utilizados durante o processamento:

```
default.files.root.dir=/data
default.file.input.sub.dir=/in
default.file.output.sub.dir=/out
default.file.processing.sub.dir=/processing
default.file.processed.sub.dir=/processed
default.file.extension=.dat
default.output.filename.extension=.done.dat
```

Isto se deve a forma de execução do programa. Ele busca um arquivo com formato ".dat" na pasta /data/in, o move para a pasta /data/processing, onde ele será aberto, lido, e no final terá seu relatório gerado em um arquivo de mesmo nome mas com formato .done.dat na pasta /data/out. Após processado, o arquivo original é movido para a pasta /data/processed, e então, a cada 5 segundos, a aplicação busca um novo arquivo.

O projeto é dividido nos seguintes pacotes:
- dto: possui o objeto que será utilizado para armazenamento dos dados do relatório
- model: possui os objetos que representam o customer, salesman e sale
- config: onde está definido o fluxo (steps) que será executado pelo spring batch
- step: aqui estão todos os steps utilizados no processo, iniciando por um processo de decisão (há arquivo para ser processado?), de onde o fluxo pode seguir para leitura dos dados, processamento do relatório, e escrita do relatório em um arquivo
- parser: possui a classe responsável por realizar o parse dos dados do arquivo. Utilizou-se o framework OpenCSV, que oferece o recurso de quebrar as linhas por um caracter delimitador, no caso "ç"
- scheduler: onde está a classe responsável por configurar o agendamento da execução a cada 5 segundos
- util: possui uma classe com métodos estáticos e utilitários para manipular arquivos

Segue exemplo de relatório gerado:

```
Summary
Salesman Count: 2
Customer Count: 2
Most Expensive Sale Id: 10
Worst Salesman Name: Renato
```

Foram acrscentados alguns poucos testes unitários, devido ao prazo para entrega do projeto. Neles utilizei assertj e datapoints do junit para validar em um único método um conjunto de dados de entrada. Além disto, pretende-se futuramente acrescentar testes que permitam mostrar o uso de mocks (mockito).

## Futuro
Tarefas para uma futura versão:
- Incluir uma poltica de retry em caso de erro no processamento
- Tornar a cron de agendamento parametrizável no arquivo de properties
- Acrescentar mais testes unitários
- Flexibilizar a estrutura onde está parser do arquivo de entrada, possibilitando o acréscimo de outros formatos (json, xml, etc.)
