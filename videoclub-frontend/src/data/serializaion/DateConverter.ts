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

import {JsonConverter, JsonCustomConvert} from 'json2typescript';
import {moment} from '@/util/Moment';

@JsonConverter
export class DateConverter implements JsonCustomConvert<Date> {
    public serialize(date: Date): string {
        return moment(date).utc(true).format('YYYY-MM-DD');
    }

    public deserialize(s: string): Date {
        return moment.utc(s).toDate();
    }
}
