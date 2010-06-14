package lookslike;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import static org.junit.Assert.*;
import static lookslike.LooksLike.*;
import static org.hamcrest.CoreMatchers.*;

public class AssertLooksLikeTest {

    @Test
    public void testRegExp() {
        String path = "C:\\Program Files";
        assertTrue(path.matches("C:\\\\.*"));
    }

    @Test
    public void useCase() {
        String output = "Killing process 1624...done\n";
        assertThat(output, looksLike("Killing process NNNN...done\n")
                                     .where("NNNN").isA(anyNumber()));
    }

    @Test
    public void useCase2() {
        String path = "C:\\Program Files";
        assertThat(path, looksLike("C:\\<path>")
                                     .where("<path>").isA(anyString()));
    }

    @Test
    public void singleTerm() {
        assertThat("1234", looksLike("NNNN").where("NNNN").isA(anyNumber()));
    }

    @Test
    public void singleTermFalse() {
        assertThat("not-a-number", not(looksLike("NNNN").where("NNNN").isA(anyNumber())));
    }

    @Test
    public void withPrologue() {
        assertThat("prologue1234", looksLike("prologueNNNN").where("NNNN").isA(anyNumber()));
        assertThat("prologue", not(looksLike("prologueNNNN").where("NNNN").isA(anyNumber())));
        assertThat("1234", not(looksLike("prologueNNNN").where("NNNN").isA(anyNumber())));
    }

    @Test
    public void withEpilogue() {
        assertThat("1234epilogue", looksLike("NNNNepilogue").where("NNNN").isA(anyNumber()));
        assertThat("epilogue", not(looksLike("NNNNepilogue").where("NNNN").isA(anyNumber())));
        assertThat("1234", not(looksLike("NNNNepilogue").where("NNNN").isA(anyNumber())));
    }

    @Test
    public void numberMatcher() {
        assertThat("12674", is(anyNumber()));
        assertThat("asdjfkl", is(not(anyNumber())));
        assertThat("a324", is(not(anyNumber())));
        assertThat("1.2", is(not(anyNumber())));
    }

    private Matcher<String> anyString() {
        return new TypeSafeMatcher<String>() {

            @Override
            public boolean matchesSafely(String item) {
                return true;
            }

            public void describeTo(Description d) {
                d.appendText("Any string");
            }
        };
    }

    private Matcher<String> anyNumber() {
        return new TypeSafeMatcher<String>(){
            @Override
            public boolean matchesSafely(String item) {
                return item.matches("[0-9]*");
            }

            public void describeTo(Description d) {
                d.appendText("is a number");
            }
        };
    }

}