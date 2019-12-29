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
import {getInfo} from '@/api/user';
import {AuthModule} from '@/store/modules/auth';

interface IUserState {
    username: string | null;
}

@Module({dynamic: true, store, name: 'user'})
class User extends VuexModule implements IUserState {
    public username: string | null = null;

    @MutationAction({mutate: ['username']})
    public async loadInfo(): Promise<IUserState> {
        let authenticated = AuthModule.isAuthenticated;
        console.log('isAuth=' + authenticated);
        if (authenticated) {
            const response = await getInfo();
            const username = response.data.username;
            return {username};
        } else {
            return {username: null};
        }
    }
}

export const UserModule = getModule(User);
