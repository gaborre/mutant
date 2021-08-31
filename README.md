# Proyecto que valida adn mutante (mutant)
## Mutant dna verification

**Requisitos para instalación local:**
- JDK 15
- Gradle wrapper 6.9.1 (Ya viene en carpeta gradle)

1. Descargar repositorio desde:
   https://github.com/gaborre/mutant.git  
     

2. Abrir proyecto con IntelliJ IDEA Community Edition Version 2020.3:
   https://www.jetbrains.com/es-es/idea/download/other.html
  
   
3. Ejecutar desde Run/Debug Configurations, las siguientes tareas:
    - clean build
    - bootRun
  
    
4. Endpoints:
    - Para probar cada endpoint, puede usar **Postman**
    - El valor de URL-LOCAL debe reemplazarlo por: 127.0.0.1, localhost, u otra ip de su equipo
    - El puerto por default que levanta el tomcat embebido es el 8080.
    - Consultar stats:  
      
        GET  
        http://URL-LOCAL:8080/api/v1/stats  
      
    - Validar adn mutante:  
      POST  
      http://URL-LOCAL:8080/api/v1/mutant  
      Content-Type: application/json
      Body:  
        {  
            "dna": [ "ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG" ]  
        }
        

5. Para acceder a la base de datos, puede usar Robo3T, ya que la base de datos es MongoDB:  
    https://robomongo.org/download  
    Tomar los datos de conexión del archivo **application.properties**
      

6. Para los endpoints en heroku cloud:  
    - Consultar stats:

        GET  
      https://mutants-check.herokuapp.com/api/v1/stats  

    - Validar adn mutante:  
      POST  
      https://mutants-check.herokuapp.com/api/v1/mutant  
      Content-Type: application/json  
      Body:  
      {  
      "dna": [ "ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG" ]  
      }