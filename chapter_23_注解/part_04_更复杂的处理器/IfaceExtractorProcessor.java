package part_04_更复杂的处理器;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("part_04_更复杂的处理器.ExtractInterface")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class IfaceExtractorProcessor extends AbstractProcessor {

    private ArrayList<Element> interfaceMethods = new ArrayList<>();
    Elements elementUtils;
    private ProcessingEnvironment processingEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for (Element e : env.getElementsAnnotatedWith(ExtractInterface.class)) {
            String interfaceName = e.getAnnotation(ExtractInterface.class).interfaceName();
            for (Element enclosed : e.getEnclosedElements()) {
                if (enclosed.getKind().equals(ElementKind.METHOD) &&
                        enclosed.getModifiers().contains(Modifier.PUBLIC) &&
                        !enclosed.getModifiers().contains(Modifier.STATIC)) {
                    interfaceMethods.add(enclosed);
                }
            }
            if (interfaceMethods.size() > 0) {
                writeInterfaceFile(interfaceName);
            }
        }
        return false;
    }

    private void writeInterfaceFile(String interfaceName) {
        try(
                Writer writer = processingEnv.getFiler()
                        .createSourceFile(interfaceName)
                        .openWriter();
        ) {
            String packageName = elementUtils
                    .getPackageOf(interfaceMethods.get(0)).toString();
            writer.write("package " + packageName + ";\n");
            writer.write("public interface " + interfaceName + "{\n");
            for (Element element : interfaceMethods) {
                ExecutableElement method = (ExecutableElement) element;
                String signature = " public ";
                signature += method.getReturnType() + " ";
                signature += method.getSimpleName();
                signature += createArgList(method.getParameters());
                writer.write(signature + ";\n");
            }
            writer.write("}");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createArgList(List<? extends VariableElement> paramenters) {
        String args = paramenters.stream()
                .map(p -> p.asType() + " " + p.getSimpleName())
                .collect(Collectors.joining(", "));
        return "(" + args + ")";
    }
}
/*
如下是一个命令行，可以在编译的时候使用处理器：
javac -processor annotations.ifx.IfaceExtractorProcessor Multiplier.java

新生成的 IMultiplier.java 的文件，
正如你通过查看上面处理器的 println() 语句所猜测的那样，
如下所示：
package annotations.ifx;
public interface IMultiplier {
    public int multiply(int x, int y);
    public int fortySeven();
    public double timesTen(double arg);
}
 */
