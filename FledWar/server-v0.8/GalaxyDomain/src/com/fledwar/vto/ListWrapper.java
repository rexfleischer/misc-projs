/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * this works as a wrapper when a list of value objects are not actually the
 * working VTO type. that basically means that when a transform needs to happen,
 * this wrapper can deal with it.
 * <p/>
 * @author REx
 */
public class ListWrapper<_W> implements Iterable<_W>
{
  public interface Transducer<_W>
  {
    public Object toNative(_W wrapping);
    
    public _W toWrapped(Object rawdata);
  }
  
  public static Transducer<BasicVTO> DefaultListWrapper = 
          new Transducer<BasicVTO>()
  {
    @Override
    public Object toNative(BasicVTO wrapping)
    {
      return wrapping.getDataAsMap();
    }
    
    @Override
    public BasicVTO toWrapped(Object rawdata)
    {
      return new BasicVTO((Map) rawdata);
    }
  };
  
  private List wrapping;
  
  private Transducer<_W> transducer;
  
  public ListWrapper(List wrapping, Transducer<_W> transducer)
  {
    this.wrapping = Objects.requireNonNull(wrapping, "wrapping");
    this.transducer = Objects.requireNonNull(transducer, "transducer");
  }

  public int size()
  {
    return wrapping.size();
  }

  public boolean isEmpty()
  {
    return wrapping.isEmpty();
  }

  public boolean add(_W e)
  {
    return wrapping.add(transducer.toNative(e));
  }

  public void clear()
  {
    wrapping.clear();
  }

  public _W get(int index)
  {
    return transducer.toWrapped(wrapping.get(index));
  }

  public void add(int index, _W element)
  {
    wrapping.add(index, transducer.toNative(element));
  }

  public _W remove(int index)
  {
    return transducer.toWrapped(wrapping.remove(index));
  }

  @Override
  public Iterator<_W> iterator()
  {
    return new Iterator<_W>()
    {
      int position = 0;

      @Override
      public boolean hasNext()
      {
        return position < size();
      }

      @Override
      public _W next()
      {
        return get(position++);
      }

      @Override
      public void remove()
      {
        throw new UnsupportedOperationException();
      }
    };
  }
}
