package ltd.fdsa.client.service.impl;

import ltd.fdsa.client.jpa.entity.UserExt;
import ltd.fdsa.client.jpa.repository.reader.UserExtReader;
import ltd.fdsa.client.jpa.repository.writer.UserExtWriter;
import ltd.fdsa.client.jpa.service.UserExtService;
import ltd.fdsa.database.jpa.service.BaseJpaService;
import org.springframework.stereotype.Service;

@Service
public class UserExtServiceImpl extends BaseJpaService<UserExt, Integer, UserExtWriter, UserExtReader> implements UserExtService {

}
