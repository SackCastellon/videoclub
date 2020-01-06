/*
 * Copyright 2020 Juan José González Abril
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {getModule, Module, MutationAction, VuexModule} from 'vuex-module-decorators';
import store from '@/store';
import {MovieModule} from '@/store/modules/movies';
import {Movie} from '@/data/Movie';

@Module({
    dynamic: true,
    store,
    name: 'cart',
    namespaced: true,
})
class CartStore extends VuexModule {

    public movies: ReadonlyArray<Movie> = [];

    public get isEmpty(): boolean {
        return this.movies.length === 0;
    }

    public get count(): number {
        return this.movies.length;
    }

    public get has(): (movie: Movie) => boolean {
        return movie => this.movies.findIndex(it => it.id === movie.id) !== -1;
    }

    public get totalPrice(): number {
        return this.movies.map(movie => movie.price).reduce(((a, b) => a + b), 0);
    }

    @MutationAction({mutate: ['movies']})
    public async add(movie: Movie) {
        const newMovies = CartModule.movies.concat(movie).sort((a, b) => a.name.localeCompare(b.name));
        localStorage.setItem('cart', JSON.stringify({movies: newMovies.map(it => it.id)}));
        return {movies: newMovies};
    }

    @MutationAction({mutate: ['movies']})
    public async remove(movie: Movie) {
        const newMovies = CartModule.movies.filter(it => it.id !== movie.id);
        localStorage.setItem('cart', JSON.stringify({movies: newMovies.map(it => it.id)}));
        return {movies: newMovies};
    }

    @MutationAction({mutate: ['movies']})
    public async load() {
        const item = localStorage.getItem('cart');
        if (item !== null) {
            const cart = JSON.parse(item) as { movies: number[] };
            if (cart.movies.length > 0) {
                const promise = await Promise.all(cart.movies.map(id => MovieModule.get(id)));
                const newMovies = promise.filter(it => it !== null) as Movie[];
                return {movies: newMovies};
            }
        }

        return {movies: []};
    }

    @MutationAction({mutate: ['movies']})
    public async clear() {
        localStorage.removeItem('cart');
        return {movies: []};
    }
}

export const CartModule = getModule(CartStore);
