package br.edu.ifsp.scl.ads.prdm.sc3039048.calculadora

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3039048.calculadora.databinding.ActivityMainBinding
import org.mariuszgromada.math.mxparser.Expression


class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(amb){
            setContentView(root)

            zeroBt.setOnClickListener { calculo.append("0") }
            umBt.setOnClickListener { calculo.append("1") }
            doisBt.setOnClickListener { calculo.append("2") }
            tresBt.setOnClickListener { calculo.append("3") }
            quatroBt.setOnClickListener { calculo.append("4") }
            cincoBt.setOnClickListener { calculo.append("5") }
            seisBt.setOnClickListener { calculo.append("6") }
            seteBt.setOnClickListener { calculo.append("7") }
            oitoBt.setOnClickListener { calculo.append("8") }
            noveBt.setOnClickListener { calculo.append("9") }

            pontoBt.setOnClickListener { calculo.append(".") }
            divisaoBt.setOnClickListener { onOperatorClick('/') }
            multiplicacaoBt.setOnClickListener { onOperatorClick('*') }
            somaBt.setOnClickListener { onOperatorClick('+') }
            subtracaoBt.setOnClickListener { onOperatorClick('-') }
            deleteBt.setOnClickListener { calculo.text = calculo.text.dropLast(1) }
            acBt.setOnClickListener { calculo.text = "" }

            resultadoBt.setOnClickListener {
                val expressao = calculo.text.toString()
                val resultadoCalculado = Expression(calculo.text.toString()).calculate()

                when{
                    expressao.endsWith("/0") -> {
                        Toast.makeText(this@MainActivity, "Não é possível dividir por zero", Toast.LENGTH_LONG).show()
                        calculo.text = ""
                    }

                    resultadoCalculado.isNaN() -> {
                        Toast.makeText(this@MainActivity, "Expressão inválida", Toast.LENGTH_LONG).show()
                        calculo.text = ""
                    }

                    else -> {calculo.text = resultadoCalculado.toString()}
                }
            }
        }
    }

    private fun onOperatorClick(novoOperador: Char) {
        val text = amb.calculo.text.toString()
        if (text.isEmpty()) {
            if (novoOperador == '-') amb.calculo.append("-")
            return
        }
        if (text.length == 1 && text[0] == '-') {
            if (novoOperador == '-') return
            return
        }

        val ultimoChar = text.last()
        val isOperador = { c: Char -> c == '+' || c == '-' || c == '*' || c == '/' }

        if (text.length >= 2) {
            val penult = text[text.length - 2]
            if ((penult == '+' || penult == '*' || penult == '/') && ultimoChar == '-') {
                when (novoOperador) {
                    '-' -> {
                        return
                    }
                    '+' -> {
                        amb.calculo.text = text.dropLast(1) // "/-" -> "/"
                        return
                    }
                    '*', '/' -> {
                        amb.calculo.text = text.dropLast(2) + novoOperador
                        return
                    }
                }
            }
        }

        if (isOperador(ultimoChar)) {
            when (novoOperador) {
                '-' -> {
                    if (ultimoChar == '*' || ultimoChar == '/') {
                        amb.calculo.text = text + "-"
                    } else {
                        amb.calculo.text = text.dropLast(1) + "-"
                    }
                }
                else -> {
                    amb.calculo.text = text.dropLast(1) + novoOperador
                }
            }
            return
        }
        amb.calculo.append(novoOperador.toString())
    }
}