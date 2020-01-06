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

import {Movie} from '@/data/Movie';
import {getModule, Module, MutationAction, VuexModule} from 'vuex-module-decorators';
import store from '@/store';
import {getMovie, getMovies, getNewMovies, getTopMovies} from '@/api/modules/movies';
import {converter} from '@/util/JsonConverter';

@Module({
    dynamic: true,
    store,
    name: 'movies',
    namespaced: true,
})
class MovieStore extends VuexModule {

    public movies: ReadonlyArray<Movie> = [];
    public newMovies: ReadonlyArray<Movie> = [];
    public topMovies: ReadonlyArray<Movie> = [];

    public get get(): (id: number) => Promise<Movie | null> {
        return async id => {
            const movie = this.movies.find(movie => movie.id === id);
            if (movie) return movie;
            else try {
                const data = (await getMovie(id)).data;
                return converter.deserializeObject(data, Movie);
            } catch (e) {
                return null;
            }
        };
    }

    @MutationAction({mutate: ['movies']})
    public async load() {
        const response = await getMovies();
        const data = converter.deserializeArray(response.data, Movie);
        return {movies: data};
    }

    @MutationAction({mutate: ['newMovies']})
    public async loadNew() {
        const response = await getNewMovies();
        const data = converter.deserializeArray(response.data, Movie);
        return {newMovies: data};
    }

    @MutationAction({mutate: ['topMovies']})
    public async loadTop() {
        const response = await getTopMovies();
        const data = converter.deserializeArray(response.data, Movie);
        return {topMovies: data};
    }
}

export const MovieModule = getModule(MovieStore);
