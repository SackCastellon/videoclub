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

const TOKEN_KEY = 'jwt_token';

export const isOnlySession = (): boolean => window.sessionStorage.getItem(TOKEN_KEY) != null;

export const getToken = (): string | null => {
    return window.sessionStorage.getItem(TOKEN_KEY) || window.localStorage.getItem(TOKEN_KEY);
};

export const setToken = (token: string, rememberToken: boolean): void => {
    if (rememberToken) {
        window.localStorage.setItem(TOKEN_KEY, token);
        window.sessionStorage.removeItem(TOKEN_KEY);
    } else {
        window.sessionStorage.setItem(TOKEN_KEY, token);
        window.localStorage.removeItem(TOKEN_KEY);
    }
};

export const clearToken = (): void => {
    window.localStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.removeItem(TOKEN_KEY);
};
