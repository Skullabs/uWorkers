package uworkers.tests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent=true )
@RequiredArgsConstructor
public class SearchMessage {

	final String query;
}
