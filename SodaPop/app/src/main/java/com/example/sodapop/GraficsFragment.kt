import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.sodapop.EstadistiquesViewModel
import com.example.sodapop.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class GraficsFragment : Fragment() {

    // ViewModel compartit amb tota l'Activity (mateix que LesMevesReceptesFragment)
    private val estadistiquesVM: EstadistiquesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grafics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvCO2 = view.findViewById<TextView>(R.id.tvCO2)
        val barChart = view.findViewById<BarChart>(R.id.barChart)
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        estadistiquesVM.dades.observe(viewLifecycleOwner) { dades ->

            // CO2
            tvCO2.text = "🌱 Energia estimada: %.5f kg CO₂".format(estadistiquesVM.calcularCO2())

            // BAR CHART — entrades al fragment LesMevesReceptes
            val barEntries = listOf(BarEntry(0f, dades.vegadesEntrada.toFloat()))
            val barDataSet = BarDataSet(barEntries, "Entrades").apply {
                color = Color.parseColor("#F06292")
                valueTextSize = 12f
            }
            barChart.apply {
                data = BarData(barDataSet)
                xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Entrades"))
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                axisRight.isEnabled = false
                description.text = ""
                animateY(600)
                invalidate()
            }

            // PIE CHART — receptes afegides vs eliminades
            // Evitem crash si els dos valors son 0
            val afegides = dades.receptesAfegides.toFloat().coerceAtLeast(0.01f)
            val eliminades = dades.receptesEliminades.toFloat().coerceAtLeast(0.01f)

            val pieEntries = listOf(
                PieEntry(afegides, "Afegides"),
                PieEntry(eliminades, "Eliminades")
            )
            val pieDataSet = PieDataSet(pieEntries, "").apply {
                colors = listOf(
                    Color.parseColor("#66BB6A"),
                    Color.parseColor("#EF5350")
                )
                valueTextSize = 13f
                valueTextColor = Color.WHITE
            }
            pieChart.apply {
                data = PieData(pieDataSet)
                isDrawHoleEnabled = true
                holeRadius = 38f
                description.text = ""
                animateY(600)
                invalidate()
            }

            // LINE CHART — minuts acumulats per sessió
            val numSessions = dades.vegadesEntrada.coerceAtLeast(1)
            val minutsPerSessio = dades.minutsUs / numSessions
            val lineEntries = (1..numSessions).map { i ->
                Entry(i.toFloat(), minutsPerSessio * i)
            }
            val lineDataSet = LineDataSet(lineEntries, "Minuts acumulats").apply {
                color = Color.parseColor("#7E57C2")
                setCircleColor(Color.parseColor("#7E57C2"))
                lineWidth = 2f
                valueTextSize = 10f
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            lineChart.apply {
                data = LineData(lineDataSet)
                xAxis.granularity = 1f
                axisRight.isEnabled = false
                description.text = ""
                animateX(600)
                invalidate()
            }
        }
    }
}