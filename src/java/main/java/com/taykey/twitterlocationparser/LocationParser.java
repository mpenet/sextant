package com.taykey.twitterlocationparser;

import com.taykey.twitterlocationparser.dto.Location;

public interface LocationParser {

    Location parseText(String text);
}
