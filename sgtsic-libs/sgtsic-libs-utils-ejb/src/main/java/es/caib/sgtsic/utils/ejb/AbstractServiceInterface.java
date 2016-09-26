/*
 * Copyright 2016 Conselleria de Salut. Govern de les Illes Balears
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.caib.sgtsic.utils.ejb;

import java.util.List;

/**
 *
 * @author gdeignacio
 * @param <T>
 */

public abstract interface AbstractServiceInterface<T> {

    void create(T entity);

    void edit(T entity);

    void remove(T entity);

    T find(Object id);
    
    T wideFind(Object id);

    List<T> findAll();

    List<T> findRange(int[] range);

    int count();

    public boolean borrable(Object id);
    
}
