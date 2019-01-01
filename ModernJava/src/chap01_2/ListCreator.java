package chap01_2;

import java.util.List;

@FunctionalInterface
public interface ListCreator<T extends List> {
    T create();
}