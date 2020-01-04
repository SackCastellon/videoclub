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

import {Shop} from '@/data/Shop';
import {getModule, Module, MutationAction, VuexModule} from 'vuex-module-decorators';
import store from '@/store';
import {getShop, getShops} from '@/api/shops';
import {converter} from '@/util/JsonConverter';

@Module({
    dynamic: true,
    store,
    name: 'shops',
})
class ShopStore extends VuexModule {

    public shops: ReadonlyArray<Shop> = [];

    public get getShop(): (id: number) => Promise<Shop | null> {
        return async id => {
            const shop = this.shops.find(shop => shop.id === id);
            if (shop) return shop;
            else try {
                const data = (await getShop(id)).data;
                return converter.deserializeObject(data, Shop);
            } catch (e) {
                return null;
            }
        };
    }

    @MutationAction({mutate: ['shops']})
    public async loadShops() {
        const response = await getShops();
        const data = converter.deserializeArray(response.data, Shop);
        return {shops: data};
    }
}

export const ShopModule = getModule(ShopStore);
