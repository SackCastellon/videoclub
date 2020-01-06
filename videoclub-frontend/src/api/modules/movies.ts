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

import {AxiosResponse} from 'axios';
import api from '@/api';
import {Movie, MovieNew, MovieUpdate} from '@/data/Movie';
import {strip} from '@/util/Strip';

export const getMovie = (id: number): Promise<AxiosResponse<Movie>> =>
    api.get(`movie/${id}`);

export const getMovies = (): Promise<AxiosResponse<Array<Movie>>> =>
    api.get('movie');

export const getNewMovies = (): Promise<AxiosResponse<Array<Movie>>> =>
    api.get('movie/new');

export const getTopMovies = (): Promise<AxiosResponse<Array<Movie>>> =>
    api.get('movie/top');

export const postMovie = (movie: MovieNew): Promise<AxiosResponse<{ movieId: number }>> =>
    api.post('movie', strip(movie, MovieNew));

export const patchMovie = (id: number, movie: MovieUpdate): Promise<AxiosResponse> =>
    api.patch(`movie/${id}`, strip(movie, MovieUpdate));
