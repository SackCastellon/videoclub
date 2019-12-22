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

const TOKEN_KEY = 'xsrfToken';

export const getToken = (): string | null =>
    window.localStorage.getItem(TOKEN_KEY);

export const setToken = (token: string): void =>
    window.localStorage.setItem(TOKEN_KEY, token);

export const clearToken = (): void =>
    window.localStorage.removeItem(TOKEN_KEY);
