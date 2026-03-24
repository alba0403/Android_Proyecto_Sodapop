import androidx.fragment.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.example.sodapop.databinding.FragmentGraficsBinding

class GraficsFragment : Fragment() {

    private var _binding: FragmentGraficsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraficsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBarChart()
        setupPieChart()
    }

    private fun setupBarChart() {
        // Datos de ejemplo — luego los sustituirás por los de DataStore
        val entries = listOf(
            BarEntry(0f, 5f),  // veces que se abrió Inicio
            BarEntry(1f, 8f),  // veces que se abrió Películas
            BarEntry(2f, 3f),  // items añadidos
            BarEntry(3f, 2f)   // items eliminados
        )

        val dataSet = BarDataSet(entries, "Estadísticas").apply {
            colors = listOf(
                Color.parseColor("#4CAF50"),
                Color.parseColor("#2196F3"),
                Color.parseColor("#FF9800"),
                Color.parseColor("#F44336")
            )
            valueTextSize = 12f
        }

        binding.barChart.apply {
            data = BarData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(
                listOf("Inicio", "Películas", "Añadidos", "Eliminados")
            )
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            axisRight.isEnabled = false
            description.isEnabled = false
            animateY(1000)
            invalidate()
        }
    }

    private fun setupPieChart() {
        val entries = listOf(
            PieEntry(5f, "Inicio"),
            PieEntry(8f, "Películas"),
            PieEntry(3f, "Añadidos"),
            PieEntry(2f, "Eliminados")
        )

        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(
                Color.parseColor("#4CAF50"),
                Color.parseColor("#2196F3"),
                Color.parseColor("#FF9800"),
                Color.parseColor("#F44336")
            )
            valueTextSize = 12f
            valueTextColor = Color.WHITE
        }

        binding.pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 40f
            setEntryLabelColor(Color.BLACK)
            animateY(1000)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}