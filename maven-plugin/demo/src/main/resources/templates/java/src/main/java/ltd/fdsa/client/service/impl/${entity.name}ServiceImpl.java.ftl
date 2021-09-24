package ltd.fdsa.client.service.impl;

import ltd.fdsa.client.jpa.entity.${entity.name};
import ltd.fdsa.client.jpa.repository.reader.${entity.name}Reader;
import ltd.fdsa.client.jpa.repository.writer.${entity.name}Writer;
import ltd.fdsa.client.jpa.service.${entity.name}Service;
import ltd.fdsa.database.jpa.service.BaseJpaService;
import org.springframework.stereotype.Service;

@Service
public class ${entity.name}ServiceImpl extends BaseJpaService<${entity.name}, Integer, ${entity.name}Writer, ${entity.name}Reader> implements ${entity.name}Service {

}
