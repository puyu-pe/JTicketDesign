package pe.puyu.SweetTicketDesign.domain.designer.text;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class SweetTable implements Iterable<SweetRow> {

    private final @NotNull SweetTableInfo _info;
    private final @NotNull List<@NotNull SweetRow> _table;

    public SweetTable(@NotNull SweetTableInfo info) {
        _table = new LinkedList<>();
        _info = new SweetTableInfo(info);
    }

    public void add(@NotNull SweetRow row) {
        _table.add(row);
    }

    public void addAll(@NotNull List<@NotNull SweetRow> rows) {
        _table.addAll(rows);
    }

    public @NotNull SweetTableInfo getInfo() {
        return _info;
    }

    @NotNull
    @Override
    public Iterator<SweetRow> iterator() {
        return _table.iterator();
    }

    @Override
    public void forEach(Consumer<? super SweetRow> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<SweetRow> spliterator() {
        return Iterable.super.spliterator();
    }
}
