# Жизненный цикл активити

Обе активити пишут события в лог с тегами `Lifecycle/Main` и `Lifecycle/Second`.
Фильтр в Logcat: `tag:Lifecycle`.

## Запуск приложения

```
Main: onCreate, savedInstanceState=false
Main: onStart
Main: onResume
```

## Переход к второй статье

```
Main:   onPause
Second: onCreate, savedInstanceState=false
Second: onStart
Second: onResume
Main:   onStop
Main:   onSaveInstanceState
```

Первая активити останавливается только после того, как вторая стала видимой.

## Возврат назад (стрелка в тулбаре или системная «Назад»)

```
Second: onPause
Main:   onRestart
Main:   onStart
Main:   onResume
Second: onStop
Second: onDestroy, isFinishing=true
```

Первая активити не пересоздаётся (`onRestart` вместо `onCreate`), поэтому счётчики лайков остаются на месте.
Вторая уничтожается окончательно, а флаг «Прочитано» она успевает вернуть через `setResult`.

## Уход в другое приложение (Home) и возврат

```
Main: onPause
Main: onStop
Main: onSaveInstanceState
Main: onRestart
Main: onStart
Main: onResume
```

## Поворот экрана (до запрета поворота)

```
Main: onPause
Main: onStop
Main: onSaveInstanceState
Main: onDestroy, isFinishing=false
Main: onCreate, savedInstanceState=true
Main: onStart
Main: onResume
```

Активити пересоздаётся целиком. Счётчики и флаг «Прочитано» сохраняются через `rememberSaveable`
(в `Bundle` из `onSaveInstanceState`), а положение переключателя во второй статье Switch восстанавливает сам.

После выполнения заданий поворот запрещён: `android:screenOrientation="portrait"` в манифесте.
