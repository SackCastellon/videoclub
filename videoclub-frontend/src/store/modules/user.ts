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
import {getUser} from '@/api/modules/user';
import {AuthModule} from '@/store/modules/auth';
import {converter} from '@/util/JsonConverter';
import {User, UserType} from '@/data/User';
import {Admin} from '@/data/Admin';
import {Member} from '@/data/Member';

interface UserState {
    user: User | null;
    admin: Admin | null;
    member: Member | null;
}

@Module({
    dynamic: true,
    store,
    name: 'user',
    namespaced: true,
})
class UserStore extends VuexModule implements UserState {

    public user: User | null = null;
    public admin: Admin | null = null;
    public member: Member | null = null;

    public get isAdmin(): boolean {
        return this.user?.type === UserType.ADMIN;
    }

    public get isMember(): boolean {
        return this.user?.type === UserType.MEMBER;
    }

    public get isLoaded(): boolean {
        return this.user !== null && (this.admin !== null || this.member !== null);
    }

    @MutationAction({mutate: ['user', 'admin', 'member']})
    public async load(): Promise<UserState> {
        if (AuthModule.isAuthenticated) {
            const {data} = await getUser();
            const user = converter.deserializeObject(data.user, User);
            switch (user.type) {
                case UserType.ADMIN:
                    const admin = converter.deserializeObject(data.admin, Admin);
                    return {user, admin, member: null};
                case UserType.MEMBER:
                    const member = converter.deserializeObject(data.member, Member);
                    return {user, admin: null, member};
            }
        } else {
            return {user: null, admin: null, member: null};
        }
    }
}

export const UserModule = getModule(UserStore);
