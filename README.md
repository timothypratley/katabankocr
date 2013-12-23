# katabankocr

Clojure implementation of the [Kata Bank OCR] [1]
[1]: http://codingdojo.org/cgi-bin/wiki.pl?KataBankOCR


## Usage

To execute with test.data:

```lein run test.data```

To save the results in a file:

```lein run test.data > test.out```

To run tests:
```lein midje```

The Kata specification contains a contradiction:
It states that the scanner can only miss pipes or underscores, not add them, and yet asks for permutations that add or remove.
I have chosen to stick with removal only as that seems like a real requirement.
This means that my test results do not match those supplied for AMB.


## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
