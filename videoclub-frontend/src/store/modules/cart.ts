/*
 * Copyright 2019 Juan José González Abril
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

const loadCart: () => (ReadonlyArray<number>) = () => {
    const item = localStorage.getItem('cart');
    if (item === null) return [];
    else return JSON.parse(item) as number[];
};

@Module({
    dynamic: true,
    store,
    name: 'cart',
})
class CartStore extends VuexModule {

    public movieIds: ReadonlyArray<number> = loadCart();

    public get count(): number {
        return this.movieIds.length;
    }

    @MutationAction({mutate: ['movieIds']})
    public async addToCart(movieId: number) {
        const newMovieIds = CartModule.movieIds.concat(movieId);
        localStorage.setItem('cart', JSON.stringify(newMovieIds));
        return {movieIds: newMovieIds};
    }

    @MutationAction({mutate: ['movieIds']})
    public async removeFromCart(movieId: number) {
        const newMovieIds = CartModule.movieIds.filter(id => id !== movieId);
        localStorage.setItem('cart', JSON.stringify(newMovieIds));
        return {movieIds: newMovieIds};
    }

    @MutationAction({mutate: ['movieIds']})
    public async clearCart() {
        localStorage.removeItem('cart');
        return {movieIds: []};
    }
}

export const CartModule = getModule(CartStore);
