package uworkers.core.endpoint;

import javax.jms.Session;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors( fluent = true )
public class Connection<T extends Session> {

	javax.jms.Connection connection;
	T session;

}
