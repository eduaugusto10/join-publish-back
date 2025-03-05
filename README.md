Para rodar o projeto em docker completo será necessário baixar 3 repositórios:

- Publisher-back é o backend que publica os novos clientes no kafka e tem os endpoints de CRUD
  https://github.com/eduaugusto10/join-publish-back.git
- Consumer-back projeto backend onde salva o novo cliente no banco de dados
  https://github.com/eduaugusto10/join-consumer-back.git
- Frontend tela para criar, listar, alterar e deletar o cliente
  https://github.com/eduaugusto10/join-frontend.git

  Como fazer:
  1. Criar uma pasta com o nome que desejar
  2. Fazer o download dos 3 repositorios as pastas deverão ficar da seguinte maneira:
     - join-consumer-back
     - join-frontend
     - join-publisher-back
     - docker-compose.yaml
  3.  Para baixar o docker-compose vá neste link https://gist.github.com/eduaugusto10/edcc36d9ad667c869a225acadd06dd44
  4.  Entre na pasta que foi criado e execute o comando: docker-compose up -d
  
