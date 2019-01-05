[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [AudioAnalysis](./index.md)

# AudioAnalysis

`data class AudioAnalysis`

### Parameters

`bars` - The time intervals of the bars throughout the track. A bar (or measure) is a segment of time defined as
a given number of beats. Bar offsets also indicate downbeats, the first beat of the measure.

`beats` - The time intervals of beats throughout the track. A beat is the basic time unit of a piece of music;
for example, each tick of a metronome. Beats are typically multiples of tatums.

`meta` - Analysis meta information (limited use)

`sections` - Sections are defined by large variations in rhythm or timbre, e.g. chorus, verse, bridge, guitar
solo, etc. Each section contains its own descriptions of tempo, key, mode, time_signature, and loudness.

`segments` - Audio segments attempts to subdivide a song into many segments, with each segment containing
a roughly consitent sound throughout its duration.

`tatums` - A tatum represents the lowest regular pulse train that a listener intuitively infers from the timing
of perceived musical events (segments).

`track` - An analysis of the track as a whole. Undocumented on Spotify's side.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `AudioAnalysis(bars: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TimeInterval`](../-time-interval/index.md)`>, beats: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TimeInterval`](../-time-interval/index.md)`>, meta: `[`AudioAnalysisMeta`](../-audio-analysis-meta/index.md)`, sections: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`AudioSection`](../-audio-section/index.md)`>, segments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`AudioSegment`](../-audio-segment/index.md)`>, tatums: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TimeInterval`](../-time-interval/index.md)`>, track: `[`TrackAnalysis`](../-track-analysis/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [bars](bars.md) | `val bars: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TimeInterval`](../-time-interval/index.md)`>`<br>The time intervals of the bars throughout the track. A bar (or measure) is a segment of time defined as a given number of beats. Bar offsets also indicate downbeats, the first beat of the measure. |
| [beats](beats.md) | `val beats: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TimeInterval`](../-time-interval/index.md)`>`<br>The time intervals of beats throughout the track. A beat is the basic time unit of a piece of music; for example, each tick of a metronome. Beats are typically multiples of tatums. |
| [meta](meta.md) | `val meta: `[`AudioAnalysisMeta`](../-audio-analysis-meta/index.md)<br>Analysis meta information (limited use) |
| [sections](sections.md) | `val sections: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`AudioSection`](../-audio-section/index.md)`>`<br>Sections are defined by large variations in rhythm or timbre, e.g. chorus, verse, bridge, guitar solo, etc. Each section contains its own descriptions of tempo, key, mode, time_signature, and loudness. |
| [segments](segments.md) | `val segments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`AudioSegment`](../-audio-segment/index.md)`>`<br>Audio segments attempts to subdivide a song into many segments, with each segment containing a roughly consitent sound throughout its duration. |
| [tatums](tatums.md) | `val tatums: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TimeInterval`](../-time-interval/index.md)`>`<br>A tatum represents the lowest regular pulse train that a listener intuitively infers from the timing of perceived musical events (segments). |
| [track](track.md) | `val track: `[`TrackAnalysis`](../-track-analysis/index.md)<br>An analysis of the track as a whole. Undocumented on Spotify's side. |