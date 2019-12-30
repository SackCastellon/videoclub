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
import {getUserData} from '@/api/user';
import {AuthModule} from '@/store/modules/auth';
import {converter} from '@/util/JsonConverter';
import {User} from '@/data/User';

interface IUserState {
    data: User | null;
}

@Module({dynamic: true, store, name: 'user'})
class UserStore extends VuexModule implements IUserState {
    public data: User | null = null;

    @MutationAction({mutate: ['data']})
    public async fetchUserData(): Promise<IUserState> {
        if (AuthModule.isAuthenticated) {
            const response = await getUserData();
            const data = converter.deserializeObject(response.data, User);
            return {data};
        } else {
            return {data: null};
        }
    }
}

export const UserModule = getModule(UserStore);
