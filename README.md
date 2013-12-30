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

You can specify to do only removal by setting the character list in perm-map to [\space].
Note also that there seem to be rules about where | and _ can occur.
As these are not explicitly stated, patterns where they appear in unexpected locations
will still be considered.

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
