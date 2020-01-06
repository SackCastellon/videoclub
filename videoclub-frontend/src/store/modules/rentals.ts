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

import {Rental} from '@/data/Rental';
import {getModule, Module, MutationAction, VuexModule} from 'vuex-module-decorators';
import store from '@/store';
import {getRental, getRentals} from '@/api/modules/rentals';
import {converter} from '@/util/JsonConverter';

@Module({
    dynamic: true,
    store,
    name: 'rentals',
    namespaced: true,
})
class RentalStore extends VuexModule {

    public rentals: ReadonlyArray<Rental> = [];

    public get get(): (id: number) => Promise<Rental | null> {
        return async id => {
            const rental = this.rentals.find(rental => rental.id === id);
            if (rental) return rental;
            else try {
                const data = (await getRental(id)).data;
                return converter.deserializeObject(data, Rental);
            } catch (e) {
                return null;
            }
        };
    }

    @MutationAction({mutate: ['rentals']})
    public async load() {
        const response = await getRentals();
        const data = converter.deserializeArray(response.data, Rental);
        return {rentals: data};
    }
}

export const RentalModule = getModule(RentalStore);
