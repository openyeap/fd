CREATE TABLE ${entity.name}(

    <#list entity.fields  as field>
    ${field.type} ${field.name};
    </#list>
   
   PRIMARY KEY( id )
);
 

