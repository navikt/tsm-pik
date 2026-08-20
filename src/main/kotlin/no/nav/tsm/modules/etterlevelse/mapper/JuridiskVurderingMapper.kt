package no.nav.tsm.modules.etterlevelse.mapper

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import no.nav.tsm.modules.etterlevelse.model.JuridiskVurderingKafkaMessage
import no.nav.tsm.modules.etterlevelse.model.Utfall
import no.nav.tsm.regulus.regula.juridisk.JuridiskUtfall
import no.nav.tsm.regulus.regula.juridisk.JuridiskVurdering

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun JuridiskVurdering.tilJuridiskVurderingKafkaMessage(
    tidsstempel: OffsetDateTime = OffsetDateTime.now()
): JuridiskVurderingKafkaMessage {
    return JuridiskVurderingKafkaMessage(
        id = UUID.fromString(id),
        tidsstempel = tidsstempel,
        eventName = eventName,
        versjon = version,
        kilde = kilde,
        versjonAvKode = versjonAvKode,
        fodselsnummer = fodselsnummer,
        sporing = sporing.mapValues { listOf(it.value) },
        lovverk = juridiskHenvisning.lovverk.kortnavn.lowercase(),
        lovverksversjon = juridiskHenvisning.lovverk.lovverksversjon.format(formatter),
        paragraf = juridiskHenvisning.paragraf,
        ledd = juridiskHenvisning.ledd,
        punktum = juridiskHenvisning.punktum,
        bokstav = juridiskHenvisning.bokstav,
        input = input,
        output = null,
        utfall = utfall.tilUtfall(),
    )
}

fun JuridiskUtfall.tilUtfall(): Utfall {
    return when (this) {
        JuridiskUtfall.VILKAR_OPPFYLT -> Utfall.VILKAR_OPPFYLT
        JuridiskUtfall.VILKAR_IKKE_OPPFYLT -> Utfall.VILKAR_IKKE_OPPFYLT
        JuridiskUtfall.VILKAR_UAVKLART -> Utfall.VILKAR_UAVKLART
    }
}
