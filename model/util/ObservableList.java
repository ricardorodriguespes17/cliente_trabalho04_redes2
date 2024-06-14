/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 10/06/2024
* Ultima alteracao.: 10/06/2024
* Nome.............: ObservableList
* Funcao...........: Permite que crie listas que podem ser
observadas para que atualize algo na aplicação.
*************************************************************** */

package model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class ObservableList<E> extends ArrayList<E> {
  private final ArrayList<Consumer<E>> listeners = new ArrayList<>();

  @Override
  public boolean add(E e) {
    boolean result = super.add(e);
    if (result) {
      notifyListeners(e);
    }
    return result;
  }

  @Override
  public void add(int index, E element) {
    super.add(index, element);
    notifyListeners(element);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean result = super.addAll(c);
    if (result) {
      for (E e : c) {
        notifyListeners(e);
      }
    }
    return result;
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    boolean result = super.addAll(index, c);
    if (result) {
      for (E e : c) {
        notifyListeners(e);
      }
    }
    return result;
  }

  private void notifyListeners(E e) {
    for (Consumer<E> listener : listeners) {
      listener.accept(e);
    }
  }

  public void addListener(Consumer<E> listener) {
    listeners.add(listener);
  }

  public void removeListener(Consumer<E> listener) {
    listeners.remove(listener);
  }
}
