CREATE TABLE ${entity.name}(

    <#list entity.fields  as field>
    ${field.type} ${field.name} ${ ll(field.name) };
    </#list>
   
   PRIMARY KEY( id )
);
 

