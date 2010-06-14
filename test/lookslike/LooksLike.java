package lookslike;

import java.util.regex.Pattern;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import static lookslike.LooksLike.*;

public class LooksLike {

    public static _looksLike looksLike(final String string) {
        return new _looksLike() {
            public _where where(final String term) {
                return new _where() {
                    public Matcher<String> isA(final Matcher<String> termMatcher) {
                        return new LookLikeMatcher(string, term, termMatcher);
                    }
                };
            }
        };
    }

    public interface _looksLike {
        _where where(String term);
    }

    public interface _where {
        Matcher<String> isA(Matcher<String> matcher);
    }

    private static class LookLikeMatcher extends TypeSafeMatcher<String> {
        private final String string;
        private final String term;
        private final Matcher<String> termMatcher;
        
        public LookLikeMatcher(String string,
                               String term,
                               Matcher<String> termMatcher)
        {
            this.string = string;
            this.term = term;
            this.termMatcher = termMatcher;
        }

        @Override
        public boolean matchesSafely(String item) {
            if(string.contains(term)) {
                String prologue = string.substring(0, string.indexOf(term));
                String epilogue = string.substring(string.indexOf(term)+term.length());
                Pattern pattern = Pattern.compile(
                                  Pattern.quote(prologue)
                                  + "(.+)"
                                  + Pattern.quote(epilogue));
                java.util.regex.Matcher matcher1 = pattern.matcher(item);
                if (!matcher1.matches()) {
                    return false;
                } else {
                    return termMatcher.matches(matcher1.group(1));
                }
            } else {
                return string.equals(item);
            }
        }

        public void describeTo(Description d) {
            d.appendText(string + " where " + term + " is a ");
            termMatcher.describeTo(d);
        }
    }
}
