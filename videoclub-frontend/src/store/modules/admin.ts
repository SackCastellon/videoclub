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
import {getAdminData} from '@/api/admin';
import {AuthModule} from '@/store/modules/auth';
import {converter} from '@/util/JsonConverter';
import {Admin} from '@/data/Admin';

@Module({
    dynamic: true,
    store,
    name: 'admin',
    namespaced: true,
})
class AdminStore extends VuexModule {

    public admin: Admin | null = null;

    @MutationAction({mutate: ['admin']})
    public async load() {
        if (AuthModule.isAuthenticated) {
            const response = await getAdminData();
            const data = converter.deserializeObject(response.data, Admin);
            return {admin: data};
        } else {
            return {admin: null};
        }
    }
}

export const AdminModule = getModule(AdminStore);
