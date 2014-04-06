package mouse.shared;

/**
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 * @param <FirstType>
 * @param <SecondType>
 */
public class Pair<FirstType, SecondType> {

    public FirstType first;
    public SecondType second;

    public Pair(FirstType firstElement, SecondType secondElement) {
        this.first = firstElement;
        this.second = secondElement;
    }
}
