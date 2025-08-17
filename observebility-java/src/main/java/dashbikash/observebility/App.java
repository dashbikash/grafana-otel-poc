package dashbikash.observebility;

import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;

/**
 * Hello world!
 */
public class App {

	private static Logger logger = LoggerFactory.getLogger(App.class);

	
	public static void main(String[] args) {
		
		logger.info("Hello World!");
		OtlpHttpSpanExporter spanExporter = OtlpHttpSpanExporter.builder()
			.setEndpoint("https://otlp-gateway-prod-ap-south-1.grafana.net/otlp/v1/traces")
			.addHeader("Authorization", "Basic ODk3OTY1OmdsY19leUp2SWpvaU1UQTVNalE1TWlJc0ltNGlPaUpxWVhaaExYUnZhMlZ1SWl3aWF5STZJamQ2TjJRMmFESnZkelJzUlRZNWJqUTNVek40V2xoMGJTSXNJbTBpT25zaWNpSTZJbkJ5YjJRdFlYQXRjMjkxZEdndE1TSjlmUT09") // Replace with your actual token
			.build();
		
		Resource resource = Resource.getDefault().toBuilder().put("service.name", "bikash-service").build();
        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
			.setPropagators(ContextPropagators.noop())
			.setTracerProvider(SdkTracerProvider.builder()
				.setResource(resource)
				.addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
				.build())
			.buildAndRegisterGlobal();
		
		
	    Tracer tracer = openTelemetry.getTracerProvider().get("ABC_XYZ1.csv", "2.0");

		String persistedTraceId = "ABCD-XYZ-123";
		SpanContext parentContext = SpanContext.create(
			persistedTraceId,
			UUID.randomUUID().toString().replace("-", "").substring(0, 16),
			TraceFlags.getSampled(),
			TraceState.getDefault()
		);

		Span span = tracer.spanBuilder("my-app")
			.setParent(io.opentelemetry.context.Context.current().with(Span.wrap(parentContext)))
			.setAttribute("file_name", UUID.randomUUID().toString()+".csv")
			.setAttribute("size", 10000)
			.setAttribute("component", "id-resolver")
			.setAttribute("account","ABCDEFGHIJKLMNOPQRSTUVWXYZ")
			.startSpan();
		logger.info("TraceId: {} (persisted)", span.getSpanContext().getTraceId());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			span.recordException(e);
		} finally {
			span.end();
			logger.info("Span ended successfully.");
		}
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}