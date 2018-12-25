/*
 * Copyright 2018 qiugang(thisisqg@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.letusplay.demo.base

import io.letusplay.base.BuildConfig
import java.util.concurrent.ConcurrentHashMap

class Bumblebee private constructor() {

  fun register(
    service: Class<*>,
    serviceImp: Any
  ) {
    services[service] = serviceImp
  }

  @Suppress("UNCHECKED_CAST")
  fun <T> visit(service: Class<T>): T? {
    if (services.containsKey(service)) {
      return services[service] as T?
    }
    if (BuildConfig.DEBUG) {
      RuntimeException(
          "${service.canonicalName} serviceImp has not register yet."
      ).printStackTrace()
      return null
    }
    throw RuntimeException("${service.canonicalName} serviceImp has not register yet.")
  }

  companion object {

    private val services = ConcurrentHashMap<Class<*>, Any>()
    private var instance: Bumblebee? = null

    fun get(): Bumblebee {
      return instance ?: synchronized(Bumblebee::class.java) {
        instance ?: Bumblebee().also { instance = it }
      }
    }
  }
}