package utm.transport.app.config.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({HibernateConfig.class})
public class DataBaseConfig
{ }
