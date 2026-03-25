using System;
using System.Diagnostics;
using System.IO;
using System.Windows.Forms;

/**
 * Native Windows Launcher for Clínica Aauca.
 * Compilar con: csc /target:winexe /out:ClinicaAauca.exe Launcher.cs
 */
public class Launcher {
    public static void Main() {
        try {
            string baseDir = AppDomain.CurrentDomain.BaseDirectory;
            string javaPath = Path.Combine(baseDir, "jre", "bin", "java.exe");
            string jarPath = Path.Combine(baseDir, "lib", "app.jar");

            // Si no existe la JRE interna, buscamos en el sistema (como respaldo)
            if (!File.Exists(javaPath)) {
                javaPath = "java"; 
            }

            if (!File.Exists(jarPath)) {
                MessageBox.Show("Error: No se encontró el archivo lib/app.jar necesario para el sistema.", 
                                "Error de Lanzamiento", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            ProcessStartInfo startInfo = new ProcessStartInfo();
            startInfo.FileName = javaPath;
            startInfo.Arguments = "-jar \"" + jarPath + "\"";
            startInfo.WorkingDirectory = baseDir;
            startInfo.UseShellExecute = false;
            startInfo.CreateNoWindow = true; // No mostrar consola negra

            Process.Start(startInfo);
        } catch (Exception ex) {
            MessageBox.Show("Ocurrió un error crítico al iniciar la aplicación:\n" + ex.Message, 
                            "Error Crítico", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }
}
