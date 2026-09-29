package com.example.cloudbuy.data.model

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

object SupabaseRepository {

    // URL base do Supabase sem '/rest/v1/' no final
    private const val SUPABASE_URL = "https://svycpdlqsqdfzkwwkglq.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_fiU5hWQkSpT0uhS5VwzaZw_23GbtUVV"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
    }

    // ─── PRODUTOS ───
    suspend fun getProducts(): List<Product> {
        return client.from("products").select().decodeList<Product>()
    }

    // ─── AUTENTICAÇÃO ───
    suspend fun login(email: String, pass: String): Boolean {
        return try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = pass
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun register(email: String, pass: String): Boolean {
        return try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = pass
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}