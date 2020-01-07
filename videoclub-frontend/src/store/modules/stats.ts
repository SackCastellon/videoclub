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

import {Stat} from '@/data/Stat';
import {getModule, Module, MutationAction, VuexModule} from 'vuex-module-decorators';
import store from '@/store';
import {getStat, getStats} from '@/api/modules/stats';
import {converter} from '@/util/JsonConverter';

@Module({
    dynamic: true,
    store,
    name: 'stats',
    namespaced: true,
})
class StatStore extends VuexModule {

    public stats: ReadonlyArray<Stat> = [];

    public get get(): (id: number) => Promise<Stat | null> {
        return async id => {
            const stat = this.stats.find(stat => stat.id === id);
            if (stat) return stat;
            else try {
                const data = (await getStat(id)).data;
                return converter.deserializeObject(data, Stat);
            } catch (e) {
                return null;
            }
        };
    }

    @MutationAction({mutate: ['stats']})
    public async load() {
        const response = await getStats();
        const data = converter.deserializeArray(response.data, Stat);
        return {stats: data};
    }
}

export const StatModule = getModule(StatStore);
