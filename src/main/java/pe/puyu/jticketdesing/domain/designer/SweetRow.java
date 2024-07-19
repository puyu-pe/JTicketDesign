package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class SweetRow implements Iterable<SweetCell>{

    private final @NotNull List<@NotNull SweetCell> _row;

    public SweetRow() {
        _row = new LinkedList<>();
    }

    public void add(@NotNull SweetCell cell) {
        _row.add(cell);
    }

    public void addAll(@NotNull List<@NotNull SweetCell> cells) {
        _row.addAll(cells);
    }

    @NotNull
    @Override
    public Iterator<SweetCell> iterator() {
        return _row.iterator();
    }

    @Override
    public void forEach(Consumer<? super SweetCell> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<SweetCell> spliterator() {
        return Iterable.super.spliterator();
    }
}
