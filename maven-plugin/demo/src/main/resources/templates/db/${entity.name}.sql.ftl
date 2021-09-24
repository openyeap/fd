CREATE TABLE ${entity.name?lower_case}(

    <#list entity.fields  as field>
    ${field.type?lower_case} ${field.name?lower_case};
    </#list>
   
   PRIMARY KEY( id )
);
 

